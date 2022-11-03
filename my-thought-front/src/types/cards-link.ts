export enum CardsType {
  TWEET,
  LINK = "LINK",
  MARKDOWN = "MARKDOWN"
}

export interface CardsLink {
  url: string,
  title: string,
  desc: string,
  image: string,
  imageAlt: string
  thingType: CardsType,
  markdown: string
}
