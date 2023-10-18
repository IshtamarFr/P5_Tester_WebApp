import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';

import { SessionApiService } from './session-api.service';
import { Session } from '../interfaces/session.interface';

describe('SessionsService', () => {
  let service: SessionApiService;
  let httpTestingController: HttpTestingController;
  let mockSession: Session;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [SessionApiService],
    });
    service = TestBed.inject(SessionApiService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should delete session by id', () => {
    service.delete('1').subscribe();
    const req = httpTestingController.expectOne(service['pathService'] + '/1');
    expect(req.request.method).toEqual('DELETE');
  });

  it('get session details', () => {
    service.detail('1').subscribe();
    const req = httpTestingController.expectOne(service['pathService'] + '/1');
    expect(req.request.method).toEqual('GET');
  });

  it('get all sessions', () => {
    service.all().subscribe();
    const req = httpTestingController.expectOne(service['pathService']);
    expect(req.request.method).toEqual('GET');
  });

  it('creates a new session', () => {
    service.create(mockSession).subscribe();
    const req = httpTestingController.expectOne(service['pathService']);
    expect(req.request.method).toEqual('POST');
  });
});
