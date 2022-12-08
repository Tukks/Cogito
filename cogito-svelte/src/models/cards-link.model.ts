export enum CardsType {
  TWEET = "TWEET",
  LINK = "LINK",
  MARKDOWN = "MARKDOWN"
}

interface Tag {
  tag: string;
  hidden: boolean;
}

interface CardsLinkType {
  id: string, // this is an uuid
  title: string,
  thingType: CardsType,
  comment: string,
  tags: Tag[],
  created: Date;
  modified: Date;
}

export interface NoteCard extends CardsLinkType {
  markdown: string,

}

interface TweetCard extends CardsLinkType {
  url: string;
  content: string,
  media: string,
  author: string,
  hashtag: string,
  html: string,


}

interface LinkCard extends CardsLinkType {
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


