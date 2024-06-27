import * as ohm from "ohm-js";
import { toAST } from "ohm-js/extras";
import grammarSource from "./grammar.ohm?raw";

export enum Duration {
  Whole = "1",
  Quarter = "1/4",
  DottedQuarter = "3/8",
  Half = "1/2",
  DottedHalf = "3/4",
  Eighth = "1/8",
  DottedEighth = "3/16",
}

export type Note = {
  type: "Note";
  letter: string;
  sharp: boolean;
  duration: Duration;
  octave: number;
};

export type Rest = {
  type: "Rest";
  duration: Duration;
};

export type AST = (Note | Rest)[];

export type Result = {
  success: boolean;
  error?: string;
  ast?: AST;
};

function map(i: number, callback: Function) {
  return (children: any[]) => callback(children[i].toAST(mapping));
}

function mapOctave(s: string | null) {
  switch (s) {
    case "'":
      return 1;
    case null:
      return 0;
    case ",":
      return -1;
    case ",,":
      return -2;
  }
}

function unmapOctave(octave: number): string {
  switch (octave) {
    case 1:
      return "'";
    case 0:
      return "";
    case -1:
      return ",";
    case -2:
      return ",,";
  }
  return "";
}

function mapDuration(s: string | null) {
  switch (s) {
    case "1":
      return Duration.Whole;
    case "2":
      return Duration.Half;
    case "2.":
      return Duration.DottedHalf;
    case "4":
    case null:
      return Duration.Quarter;
    case "4.":
      return Duration.DottedQuarter;
    case "8":
      return Duration.Eighth;
    case "8.":
      return Duration.DottedEighth;
  }
}

function unmapDuration(s: string | null) {
  switch (s) {
    case Duration.Whole:
      return "1";
    case Duration.Half:
      return "2";
    case Duration.DottedHalf:
      return "2.";
    case Duration.Quarter:
      return "4";
    case Duration.DottedQuarter:
      return "4.";
    case Duration.Eighth:
      return "8";
    case Duration.DottedEighth:
      return "8.";
  }
}

const mapping = {
  Note: {
    letter: 0,
    sharp: map(1, (s: string | null) => s === "#"),
    duration: map(2, mapDuration),
    octave: map(3, mapOctave),
  },
  Rest: {
    duration: map(1, mapDuration),
  },
};

export class Grammar {
  private grammar: ohm.Grammar;

  constructor() {
    this.grammar = ohm.grammar(grammarSource);
  }

  public match(s: string): Result {
    const result = this.grammar.match(s);

    const success = result.succeeded();
    const error = result.message;
    const ast = success ? toAST(result, mapping) : undefined;

    return {
      success,
      error,
      ast: ast as AST,
    };
  }

  public toString(ast: AST): string {
    const result = [];
    for (const note of ast) {
      const duration = unmapDuration(note.duration);
      if (note.type === "Note") {
        const sharp = note.sharp ? "#" : "";
        const octave = unmapOctave(note.octave);
        result.push(`${note.letter}${sharp}${duration}${octave}`);
      } else {
        result.push(`r${duration}`);
      }
    }
    return result.join(" ");
  }
}
