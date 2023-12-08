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
import { Session } from '../../interfaces/session.interface';
import { of } from 'rxjs';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let router: Router;
  let sessionApiService: SessionApiService;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
    },
  };

  const mockSession: Session = {
    name: 'mockName',
    description: 'mockDescription',
    date: new Date(),
    teacher_id: 1,
    users: [],
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
    sessionApiService = TestBed.inject(SessionApiService);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('non admin should be redirected to sessions at start', () => {
    //Given
    let navigateSpy = jest.spyOn(router, 'navigate');
    mockSessionService.sessionInformation.admin = false;

    //When
    component.ngOnInit();

    //Then
    expect(navigateSpy).toHaveBeenCalledWith(['/sessions']);
  });

  it('admin should not be redirected at start', () => {
    //Given
    let navigateSpy = jest.spyOn(router, 'navigate');
    mockSessionService.sessionInformation.admin = true;
    jest.spyOn(router, 'url', 'get').mockReturnValue('/sessions/update/42');

    //When
    component.ngOnInit();

    //Then
    expect(navigateSpy).not.toHaveBeenCalledWith(['/sessions']);
  });

  it('non admin should be redirected on update', () => {
    //Given
    let navigateSpy = jest.spyOn(router, 'navigate');
    mockSessionService.sessionInformation.admin = false;
    let sessionApiServiceSpy = jest
      .spyOn(sessionApiService, 'detail')
      .mockReturnValue(of(mockSession));

    jest.spyOn(router, 'url', 'get').mockReturnValue('/sessions/update/42');

    //When
    component.ngOnInit();

    //Then
    expect(navigateSpy).toHaveBeenCalledWith(['/sessions']);
    expect(component['onUpdate']).toBe(true);
    expect(sessionApiServiceSpy).toHaveBeenCalled();
  });

  it('leave page should navigate to sessions', () => {
    //Given
    let navigateSpy = jest.spyOn(router, 'navigate');

    //When
    component['exitPage']('test');

    //Then
    expect(navigateSpy).toHaveBeenCalledWith(['sessions']);
  });

  it('create should submit form and call service', () => {
    //Given
    let sessionApiServiceSpy = jest
      .spyOn(sessionApiService, 'create')
      .mockReturnValue(of(mockSession));

    //When
    component.submit();

    //Then
    expect(sessionApiServiceSpy).toHaveBeenCalled();
  });

  it('update should submit form and call service', () => {
    //Given
    let sessionApiServiceSpy = jest
      .spyOn(sessionApiService, 'update')
      .mockReturnValue(of(mockSession));
    component['onUpdate'] = true;

    //When
    component.submit();

    //Then
    expect(sessionApiServiceSpy).toHaveBeenCalled();
  });
});
