import {Directive, ElementRef, HostListener, Input} from '@angular/core';

@Directive({
  selector: 'img[appImgFallback]'
})
export class ImgFallbackDirective {

  @Input() appImgFallback: string | undefined = undefined;

  constructor(private eRef: ElementRef) {
  }

  @HostListener('error')
  loadFallbackOnError() {
    const element: HTMLImageElement = <HTMLImageElement>this.eRef.nativeElement;
    element.src = this.appImgFallback || 'assets/image/no_image_fallback.png';
  }
}
