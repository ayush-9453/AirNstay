import { TestBed } from '@angular/core/testing';

import { TemopraryStoreService } from './temoprary-store.service';

describe('TemopraryStoreService', () => {
  let service: TemopraryStoreService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TemopraryStoreService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
