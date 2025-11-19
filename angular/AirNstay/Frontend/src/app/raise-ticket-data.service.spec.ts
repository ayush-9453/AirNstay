import { TestBed } from '@angular/core/testing';

import { RaiseTicketDataService } from './raise-ticket-data.service';

describe('RaiseTicketDataService', () => {
  let service: RaiseTicketDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RaiseTicketDataService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
