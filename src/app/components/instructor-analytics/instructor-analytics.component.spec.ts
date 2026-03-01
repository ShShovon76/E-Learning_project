import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InstructorAnalyticsComponent } from './instructor-analytics.component';

describe('InstructorAnalyticsComponent', () => {
  let component: InstructorAnalyticsComponent;
  let fixture: ComponentFixture<InstructorAnalyticsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InstructorAnalyticsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InstructorAnalyticsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
