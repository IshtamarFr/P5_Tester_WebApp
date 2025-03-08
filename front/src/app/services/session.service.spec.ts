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

  it('service islogged should be falsy at start', () => {
    expect(service.$isLogged()).not.toBeTruthy;
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('logIn should make service islogged to be truthy', () => {
    //Given

    //When
    service.logIn(mockUser);

    //Then
    expect(service.isLogged).toBeTruthy;
  });

  it('logOut should make service islogged to be falsy', () => {
    //Given

    //When
    service.logOut();

    //Then
    expect(service.isLogged).not.toBeTruthy;
  });
});
