import {TestBed} from '@angular/core/testing';

import {ThoughtsService} from './thoughts.service';

describe('ThoughtsService', () => {
  let service: ThoughtsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ThoughtsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
