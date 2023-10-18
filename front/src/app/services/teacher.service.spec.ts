import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';

import { TeacherService } from './teacher.service';
import { Teacher } from '../interfaces/teacher.interface';

describe('TeacherService', () => {
  let service: TeacherService;
  let httpClient: HttpClient;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(TeacherService);
    httpClient = TestBed.inject(HttpClient);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get all teachers', () => {
    const response: Teacher[] = [
      {
        id: 42,
        lastName: 'mockLastName',
        firstName: 'mockFirstName',
        createdAt: new Date(),
        updatedAt: new Date(),
      },
    ];

    httpClient
      .get<Teacher[]>(service['pathService'])
      .subscribe((data) => expect(data).toEqual(response));

    const req = httpTestingController.expectOne(service['pathService']);
    expect(req.request.method).toEqual('GET');
    req.flush(response);
  });
});
