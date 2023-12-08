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

  it('delete session by id should call http method', () => {
    //Given

    //When
    service.delete('1').subscribe();

    //Then
    const req = httpTestingController.expectOne(service['pathService'] + '/1');
    expect(req.request.method).toEqual('DELETE');
  });

  it('get session details should call http method', () => {
    //Given

    //When
    service.detail('1').subscribe();

    //Then
    const req = httpTestingController.expectOne(service['pathService'] + '/1');
    expect(req.request.method).toEqual('GET');
  });

  it('get all sessions should call http method', () => {
    //Given

    //When
    service.all().subscribe();

    //Then
    const req = httpTestingController.expectOne(service['pathService']);
    expect(req.request.method).toEqual('GET');
  });

  it('create a new session should call http method', () => {
    //Given

    //When
    service.create(mockSession).subscribe();

    //Then
    const req = httpTestingController.expectOne(service['pathService']);
    expect(req.request.method).toEqual('POST');
  });

  it('update a session should call http method', () => {
    //Given

    //When
    service.update('999', mockSession).subscribe();

    //Then
    const req = httpTestingController.expectOne(
      service['pathService'] + '/999'
    );
    expect(req.request.method).toEqual('PUT');
  });

  it('participates should work and call http method', () => {
    //Given

    //When
    service.participate('1', '999').subscribe();

    //Then
    const req = httpTestingController.expectOne(
      service['pathService'] + '/1/participate/999'
    );
    expect(req.request.method).toEqual('POST');
  });

  it('unParticipates should work and call http method', () => {
    //Given

    //When
    service.unParticipate('1', '999').subscribe();

    //Then
    const req = httpTestingController.expectOne(
      service['pathService'] + '/1/participate/999'
    );
    expect(req.request.method).toEqual('DELETE');
  });
});
