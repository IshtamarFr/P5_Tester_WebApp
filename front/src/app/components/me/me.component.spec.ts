import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
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
});
