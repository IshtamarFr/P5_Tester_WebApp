import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed, fakeAsync } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import {
  BrowserAnimationsModule,
  NoopAnimationsModule,
} from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';
import { Router, Routes, provideRouter } from '@angular/router';
import { SessionService } from '../../../../services/session.service';
import { routes } from '../../../../app-routing.module';
import { Session } from '../../interfaces/session.interface';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let router: Router;
  let navigateSpy: jest.SpyInstance<Promise<Boolean>>;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
    },
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes(routes),
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,
        NoopAnimationsModule,
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        SessionApiService,
      ],
      declarations: [FormComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    router = TestBed.inject(Router);
    navigateSpy = jest.spyOn(router, 'navigate');
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should redirect non admin', () => {
    mockSessionService.sessionInformation.admin = false;
    component.ngOnInit();
    expect(navigateSpy).toHaveBeenCalledWith(['/sessions']);
  });

  it('should not redirect admin on update', () => {
    mockSessionService.sessionInformation.admin = true;
    jest.spyOn(router, 'url', 'get').mockReturnValue('/sessions/update/42');
    component.ngOnInit();
    expect(navigateSpy).not.toHaveBeenCalledWith(['/sessions']);
  });

  it('should redirect non admin on update', () => {
    mockSessionService.sessionInformation.admin = false;
    jest.spyOn(router, 'url', 'get').mockReturnValue('/sessions/update/42');
    component.ngOnInit();
    expect(navigateSpy).toHaveBeenCalledWith(['/sessions']);
    expect(component['onUpdate']).toBe(true);
  });

  it('should leave page', () => {
    component['exitPage']('test');
    expect(navigateSpy).toHaveBeenCalledWith(['sessions']);
  });
});
