import { HttpClientModule } from '@angular/common/http';
import {
  ComponentFixture,
  TestBed,
  fakeAsync,
  tick,
} from '@angular/core/testing';
import { NgControl, ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from '../../../../services/session.service';

import { DetailComponent } from './detail.component';
import { routes } from '../../../../app-routing.module';
import { Router } from '@angular/router';
import { SessionApiService } from '../../services/session-api.service';
import { of } from 'rxjs';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { Session } from '../../interfaces/session.interface';

describe('DetailComponent', () => {
  let component: DetailComponent;
  let sessionApiService: SessionApiService;
  let fixture: ComponentFixture<DetailComponent>;
  let router: Router;
  let navigateSpy: jest.SpyInstance<Promise<Boolean>>;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1,
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
        MatSnackBarModule,
        NoopAnimationsModule,
        ReactiveFormsModule,
      ],
      declarations: [DetailComponent],
      providers: [{ provide: SessionService, useValue: mockSessionService }],
    }).compileComponents();

    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    router = TestBed.inject(Router);
    sessionApiService = TestBed.inject(SessionApiService);
    navigateSpy = jest.spyOn(router, 'navigate');
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should get back', () => {
    let componentBack = jest.spyOn(component, 'back');
    component.back();
    expect(componentBack).toBeDefined();
    expect(componentBack).toHaveBeenCalled();
  });

  it('should init', () => {
    let sessionApiServiceSpy = jest
      .spyOn(sessionApiService, 'detail')
      .mockReturnValue(of(mockSession));
    component['sessionId'] = '42';
    component.ngOnInit();
    expect(sessionApiServiceSpy).toHaveBeenCalledWith('42');
  });

  it('should delete session', () => {
    component['sessionId'] = '42';
    let sessionApiServiceSpy = jest
      .spyOn(sessionApiService, 'delete')
      .mockReturnValue(of('test'));
    let navigateSpy = jest.spyOn(router, 'navigate');
    component.delete();
    expect(sessionApiServiceSpy).toHaveBeenCalledWith('42');
    expect(navigateSpy).toHaveBeenCalledWith(['sessions']);
  });

  it('should participate', () => {
    component['sessionId'] = '1';
    component['userId'] = '2';

    let sessionApiServiceSpy = jest
      .spyOn(sessionApiService, 'participate')
      .mockReturnValue(of(void 0));
    component.participate();
    expect(sessionApiServiceSpy).toHaveBeenCalledWith('1', '2');
  });

  it('should unParticipate', () => {
    component['sessionId'] = '3';
    component['userId'] = '4';

    let sessionApiServiceSpy = jest
      .spyOn(sessionApiService, 'unParticipate')
      .mockReturnValue(of(void 0));
    component.unParticipate();
    expect(sessionApiServiceSpy).toHaveBeenCalledWith('3', '4');
  });
});
