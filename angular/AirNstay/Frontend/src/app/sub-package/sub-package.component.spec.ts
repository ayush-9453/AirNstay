import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SubPackageComponent } from './sub-package.component';

describe('SubPackageComponent', () => {
  let component: SubPackageComponent;
  let fixture: ComponentFixture<SubPackageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SubPackageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SubPackageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
