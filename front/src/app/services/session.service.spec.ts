import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

describe('SessionService', () => {
  let service: SessionService;

  let mockUser: SessionInformation = {
    token: 'mockToken',
    type: 'mockType',
    id: 1,
    username: 'mockUserName',
    firstName: 'mockFirstName',
    lastName: 'mockLastName',
    admin: true,
  };

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should be logged in', () => {
    service.logIn(mockUser);
    expect(service.isLogged).toBeTruthy;
  });

  it('should be logged out', () => {
    service.logOut();
    expect(service.isLogged).not.toBeTruthy;
  });
});
