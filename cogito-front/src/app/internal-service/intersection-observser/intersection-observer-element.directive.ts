import { Directive, ElementRef, OnDestroy, OnInit } from '@angular/core';
import { IntersectionObserverGridDirective } from './intersection-observer-grid.directive';

@Directive({
  selector: '[appIntersectionObserverElement]',
  exportAs: 'intersection',
})
export class IntersectionObserverElementDirective implements OnDestroy, OnInit {
  public isIntersecting: boolean = false;
  // These are just some human-friendly constants to make the HTML template a bit more
  // readable when being consumed as part of SWTCH/CASE statements.
  public IS_INTERSECTING: boolean = true;

  private elementRef: ElementRef;
  private parent: IntersectionObserverGridDirective;

  // I initialize the intersection observer directive.
  constructor(
    parent: IntersectionObserverGridDirective,
    elementRef: ElementRef
  ) {
    this.parent = parent;
    this.elementRef = elementRef;

    // By default, we're going to assume that the host element is NOT intersecting.
    // Then, we'll use the IntersectionObserver to asynchronously check for changes
    // in viewport visibility.
    this.isIntersecting = false;
  }

  // ---
  // PUBLIC METHODS.
  // ---

  // I get called once when the host element is being destroyed.
  public ngOnDestroy(): void {
    this.parent.remove(this.elementRef.nativeElement);
  }

  // I get called once after the inputs have been bound for the first time.
  public ngOnInit(): void {
    this.parent.add(
      this.elementRef.nativeElement,
      (isIntersecting: boolean) => {
        this.isIntersecting = isIntersecting;
      }
    );
  }
}
