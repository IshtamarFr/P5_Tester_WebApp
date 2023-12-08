import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';

import { TeacherService } from './teacher.service';

describe('TeacherService', () => {
  let service: TeacherService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [TeacherService],
    });
    service = TestBed.inject(TeacherService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('get all teachers should call http method', () => {
    //Given

    //When
    service.all().subscribe();

    //Then
    const req = httpTestingController.expectOne(service['pathService']);
    expect(req.request.method).toEqual('GET');
  });

  it('get teacher by id should call http method', () => {
    //Given

    //When
    service.detail('42').subscribe();

    //Then
    const req = httpTestingController.expectOne(service['pathService'] + '/42');
    expect(req.request.method).toEqual('GET');
  });
});
