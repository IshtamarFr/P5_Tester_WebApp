import { HttpClientModule } from '@angular/common/http';
import {
  ComponentFixture,
  TestBed,
  fakeAsync,
  tick,
} from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { SessionService } from '../../services/session.service';

import { MeComponent } from './me.component';
import { RouterTestingModule } from '@angular/router/testing';
import { Router } from '@angular/router';
import { User } from '../../interfaces/user.interface';
import { UserService } from '../../services/user.service';
import { of } from 'rxjs';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let userService: UserService;
  let router: Router;

  const mockUser: User = {
    id: 1,
    email: 'user1@test.com',
    lastName: 'mockLN',
    firstName: 'mockFN',
    admin: false,
    password: 'abcdefg',
    createdAt: new Date(),
  };

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1,
    },
    logOut(): void {},
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      providers: [{ provide: SessionService, useValue: mockSessionService }],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        RouterTestingModule,
        NoopAnimationsModule,
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    userService = TestBed.inject(UserService);
    router = TestBed.inject(Router);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should init with user id', () => {
    let userServiceSpy = jest
      .spyOn(userService, 'getById')
      .mockReturnValue(of(mockUser));

    component.ngOnInit();
    expect(component['user']?.firstName).toBe('mockFN');
    expect(userServiceSpy).toHaveBeenCalledWith('1');
  });

  it('should get back', () => {
    let componentBack = jest.spyOn(component, 'back');
    component.back();
    expect(componentBack).toBeDefined();
    expect(componentBack).toHaveBeenCalled();
  });

  it('should delete user', () => {
    let userServiceSpy = jest
      .spyOn(userService, 'delete')
      .mockReturnValue(of(void 0));
    let navigateSpy = jest.spyOn(router, 'navigate');
    component.delete();
    expect(userServiceSpy).toHaveBeenCalled();
    expect(navigateSpy).toHaveBeenCalledWith(['/']);
  });
});
