import { Directive } from "@angular/core";

@Directive({
  selector: "[appIntersectionObserverGrid]"
})
export class IntersectionObserverGridDirective {

  private mapping: Map<Element, Function>;
  private observer: IntersectionObserver;

  // I initialize the intersection observer parent directive.
  constructor() {

    // As each observable child attaches itself to the parent observer, we need to
    // map Elements to Callbacks so that when an Element's intersection changes,
    // we'll know which callback to invoke. For this, we'll use an ES6 Map.
    this.mapping = new Map();

    this.observer = new IntersectionObserver(
      (entries: IntersectionObserverEntry[]) => {

        for (const entry of entries) {

          const callback = this.mapping.get(entry.target);

          (callback && callback(entry.isIntersecting));

        }

      },
      {
        // This classifies the "intersection" as being a bit outside the
        // viewport. The intent here is give the elements a little time to react
        // to the change before the element is actually visible to the user.
        rootMargin: "800px 0px 800px 0px",
        threshold: [0, 0.10, 0.20, 0.30, 0.40,0.50, 0.6, 0.7, 0.8, 0.9, 1],
      }
    );

  }

  // ---
  // PUBLIC METHODS.
  // ---

  // I add the given Element for intersection observation. When the intersection status
  // changes, the given callback is invoked with the new status.
  public add(element: HTMLElement, callback: Function): void {

    this.mapping.set(element, callback);
    this.observer.observe(element);

  }


  // I get called once when the host element is being destroyed.
  public ngOnDestroy(): void {

    this.mapping.clear();
    this.observer.disconnect();

  }


  // I remove the given Element from intersection observation.
  public remove(element: HTMLElement): void {

    this.mapping.delete(element);
    this.observer.unobserve(element);

  }

}

