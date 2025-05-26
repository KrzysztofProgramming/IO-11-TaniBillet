import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TicketPurchaseOptionsModalComponent } from './ticket-purchase-options-modal.component';

describe('TicketPurchaseOptionsModalComponent', () => {
  let component: TicketPurchaseOptionsModalComponent;
  let fixture: ComponentFixture<TicketPurchaseOptionsModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TicketPurchaseOptionsModalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TicketPurchaseOptionsModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
