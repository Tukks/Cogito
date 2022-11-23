export enum CardsType {
  TWEET = "TWEET",
  LINK = "LINK",
  MARKDOWN = "MARKDOWN"
}

export interface Tag {
  tag: string;
  hidden: boolean;
}

export interface CardsLink {
  id: string, // this is an uuid
  title: string,
  thingType: CardsType,
  comment: string,
  tags: Tag[],
  created: Date;
  modified: Date;
}

export interface NoteCard extends CardsLink {
  markdown: string,

}

export interface TweetCard extends CardsLink {
  url: string;
  content: string,
  media: string,
  author: string,
  hashtag: string,
  html: string,


}

export interface LinkCard extends CardsLink {
  url: string;
  desc: string,
  image: string,
  imageAlt: string,
  domain: string,


}

export type CardType =
  LinkCard
  | TweetCard
  | NoteCard;
