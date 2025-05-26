import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TicketPurchaseModalComponent } from './ticket-purchase-modal.component';

describe('TicketPurchaseModalComponent', () => {
  let component: TicketPurchaseModalComponent;
  let fixture: ComponentFixture<TicketPurchaseModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TicketPurchaseModalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TicketPurchaseModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
