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

  it('should register', () => {
    let registerRequest: RegisterRequest = {
      email: 'test@test.com',
      firstName: 'mockFN',
      lastName: 'mockLN',
      password: '123456',
    };
    service.register(registerRequest).subscribe();
    const req = httpTestingController.expectOne(
      service['pathService'] + '/register'
    );
    expect(req.request.method).toEqual('POST');
  });

  it('should login', () => {
    let loginRequest: LoginRequest = {
      email: 'test@test.com',
      password: '123456',
    };
    service.login(loginRequest).subscribe();
    const req = httpTestingController.expectOne(
      service['pathService'] + '/login'
    );
    expect(req.request.method).toEqual('POST');
  });
});
