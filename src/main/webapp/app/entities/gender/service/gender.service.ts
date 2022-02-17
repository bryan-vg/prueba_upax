import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IGender, getGenderIdentifier } from '../gender.model';

export type EntityResponseType = HttpResponse<IGender>;
export type EntityArrayResponseType = HttpResponse<IGender[]>;

@Injectable({ providedIn: 'root' })
export class GenderService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/genders');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(gender: IGender): Observable<EntityResponseType> {
    return this.http.post<IGender>(this.resourceUrl, gender, { observe: 'response' });
  }

  update(gender: IGender): Observable<EntityResponseType> {
    return this.http.put<IGender>(`${this.resourceUrl}/${getGenderIdentifier(gender) as number}`, gender, { observe: 'response' });
  }

  partialUpdate(gender: IGender): Observable<EntityResponseType> {
    return this.http.patch<IGender>(`${this.resourceUrl}/${getGenderIdentifier(gender) as number}`, gender, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IGender>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IGender[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addGenderToCollectionIfMissing(genderCollection: IGender[], ...gendersToCheck: (IGender | null | undefined)[]): IGender[] {
    const genders: IGender[] = gendersToCheck.filter(isPresent);
    if (genders.length > 0) {
      const genderCollectionIdentifiers = genderCollection.map(genderItem => getGenderIdentifier(genderItem)!);
      const gendersToAdd = genders.filter(genderItem => {
        const genderIdentifier = getGenderIdentifier(genderItem);
        if (genderIdentifier == null || genderCollectionIdentifiers.includes(genderIdentifier)) {
          return false;
        }
        genderCollectionIdentifiers.push(genderIdentifier);
        return true;
      });
      return [...gendersToAdd, ...genderCollection];
    }
    return genderCollection;
  }
}
