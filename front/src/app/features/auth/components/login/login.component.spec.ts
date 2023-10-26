import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import {
  BrowserAnimationsModule,
  NoopAnimationsModule,
} from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';

import { LoginComponent } from './login.component';
import { SessionService } from '../../../../services/session.service';
import { AuthService } from '../../services/auth.service';
import { routes } from '../../../../app-routing.module';
import { Router } from '@angular/router';
import { SessionInformation } from '../../../../interfaces/sessionInformation.interface';
import { of } from 'rxjs';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: AuthService;
  let router: Router;

  let mockSessionInformation: SessionInformation = {
    token: 'azertyuiop',
    type: 'mockType',
    id: 42,
    username: 'mockUser',
    firstName: 'mockFN',
    lastName: 'mockLN',
    admin: false,
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [SessionService],
      imports: [
        RouterTestingModule.withRoutes(routes),
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        NoopAnimationsModule,
      ],
    }).compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    router = TestBed.inject(Router);
    authService = TestBed.inject(AuthService);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should submit', () => {
    let authServiceSpy = jest
      .spyOn(authService, 'login')
      .mockReturnValue(of(mockSessionInformation));
    component.submit();
    expect(authServiceSpy).toHaveBeenCalled();
  });
});
