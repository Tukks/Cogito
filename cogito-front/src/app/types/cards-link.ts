export enum CardsType {
  TWEET = "TWEET",
  LINK = "LINK",
  MARKDOWN = "MARKDOWN"
}

export interface Tag {
  tag: string;
}

export interface CardsLink {
  title: string,
  thingType: CardsType,
  id: number,
  comment: string,
  tags: Tag[],

}

export interface NoteCard extends CardsLink {
  markdown: string,

  url: never;
  content: never,
  media: never,
  author: never,
  hashtag: never,
  desc: never,
  image: never,
  imageAlt: never
  html: never,
  domain: never,
}

export interface TweetCard extends CardsLink {
  url: string;
  content: string,
  media: string,
  author: string,
  hashtag: string,
  html: string,

  markdown: never,

  desc: never,
  image: never,
  imageAlt: never,
  domain: never,

}

export interface LinkCard extends CardsLink {
  url: string;
  desc: string,
  image: string,
  imageAlt: string,
  domain: string,

  content: never,
  media: never,
  author: never,
  hashtag: never,
  markdown: never,
  html: never

}

export type CardType =
  | LinkCard
  | TweetCard
  | NoteCard;
