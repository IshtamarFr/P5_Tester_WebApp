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

  it('should get all teachers', () => {
    service.all().subscribe();
    const req = httpTestingController.expectOne(service['pathService']);
    expect(req.request.method).toEqual('GET');
  });

  it('should get teacher by id', () => {
    service.detail('42').subscribe();
    const req = httpTestingController.expectOne(service['pathService'] + '/42');
    expect(req.request.method).toEqual('GET');
  });
});
