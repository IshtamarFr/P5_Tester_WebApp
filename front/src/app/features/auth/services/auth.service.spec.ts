import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { AuthService } from './auth.service';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
import { RegisterRequest } from '../interfaces/registerRequest.interface';
import { LoginRequest } from '../interfaces/loginRequest.interface';

describe('AuthService', () => {
  let service: AuthService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AuthService],
    });
    service = TestBed.inject(AuthService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('register should call http method', () => {
    //Given
    let registerRequest: RegisterRequest = {
      email: 'test@test.com',
      firstName: 'mockFN',
      lastName: 'mockLN',
      password: '123456',
    };

    //When
    service.register(registerRequest).subscribe();

    //Then
    const req = httpTestingController.expectOne(
      service['pathService'] + '/register'
    );
    expect(req.request.method).toEqual('POST');
  });

  it('should login', () => {
    //Given
    let loginRequest: LoginRequest = {
      email: 'test@test.com',
      password: '123456',
    };

    //When
    service.login(loginRequest).subscribe();

    //Then
    const req = httpTestingController.expectOne(
      service['pathService'] + '/login'
    );
    expect(req.request.method).toEqual('POST');
  });
});
