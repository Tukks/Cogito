export enum CardsType {
  TWEET,
  LINK = "LINK",
  MARKDOWN = "MARKDOWN"
}

export interface CardsLink {
  id: number,
  url: string,
  title: string,
  desc: string,
  image: string,
  imageAlt: string
  thingType: CardsType,
  markdown: string
}
