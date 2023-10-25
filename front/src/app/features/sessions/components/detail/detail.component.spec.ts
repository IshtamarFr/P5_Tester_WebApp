import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NgControl, ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from '../../../../services/session.service';

import { DetailComponent } from './detail.component';
import { routes } from '../../../../app-routing.module';
import { Router } from '@angular/router';
import { SessionApiService } from '../../services/session-api.service';

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

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes(routes),
        HttpClientModule,
        MatSnackBarModule,
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
    let sessionApiServiceSpy = jest.spyOn(sessionApiService, 'detail');
    component['sessionId'] = '42';
    component.ngOnInit();
    expect(sessionApiServiceSpy).toHaveBeenCalledWith('42');
  });
});
