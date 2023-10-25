import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';
import { Router } from '@angular/router';
import { SessionService } from '../../../../services/session.service';
import { routes } from '../../../../app-routing.module';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let router: Router;
  let navigateSpy: jest.SpyInstance<Promise<Boolean>>;
  let sessionApiService: SessionApiService;

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
      providers: [{ provide: SessionService, useValue: mockSessionService }],
      declarations: [FormComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    router = TestBed.inject(Router);
    navigateSpy = jest.spyOn(router, 'navigate');
    sessionApiService = TestBed.inject(SessionApiService);
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

  it('should submit form on create', () => {
    let sessionApiServiceSpy = jest.spyOn(sessionApiService, 'create');
    component.submit();
    expect(sessionApiServiceSpy).toHaveBeenCalled();
  });

  it('should submit form on update', () => {
    let sessionApiServiceSpy = jest.spyOn(sessionApiService, 'update');
    component['onUpdate'] = true;
    component.submit();
    expect(sessionApiServiceSpy).toHaveBeenCalled();
  });
});
