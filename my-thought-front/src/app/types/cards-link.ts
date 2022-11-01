export enum CardsType {
  TWEET,
  LINK,
  MARKDOWN
}

export interface CardsLink {
  url: string,
  title: string,
  desc: string,
  image: string,
  imageAlt: string
  type: CardsType
}
