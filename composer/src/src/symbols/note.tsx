import { Note as GNote, Rest as GRest, Duration } from "../grammar/grammar";
import { QuarterNote } from "./quarter_note";
import { EighthNote } from "./eighth_note";
import { WholeNote } from "./whole_note";
import { QuarterRest } from "./quarter_rest";
import { HalfNote } from "./half_note";
import { Dot } from "./dot";
import { Sharp } from "./sharp";
import { Line } from "./line";

export type NoteProps = {
  note: GNote | GRest;
  index: number;
  isCurrent: boolean;
};

export type RestProps = {
  duration: Duration;
  index: number;
};

const NOTE_HEIGHT = 11.86;
const ASCII_A = 97;
const A_OFFSET = -2.2;
const HORIZONTAL_SPACE = 60;
const CURRENT_COLOR = "#8fff3d";

function Rest({ duration, index }: RestProps) {
  const x = index * HORIZONTAL_SPACE;
  switch (duration) {
    case Duration.Quarter:
      return <QuarterRest x={x} y={0} />;
  }
}

function getNoteComponent(note: GNote) {
  switch (note.duration) {
    case Duration.Whole:
      return WholeNote;
    case Duration.Quarter:
    case Duration.DottedQuarter:
      return QuarterNote;
    case Duration.Eighth:
    case Duration.DottedEighth:
      return EighthNote;
    case Duration.Half:
    case Duration.DottedHalf:
      return HalfNote;
  }
}

function isDotted(duration: Duration): boolean {
  switch (duration) {
    case Duration.DottedEighth:
    case Duration.DottedQuarter:
    case Duration.DottedHalf:
      return true;
    default:
      return false;
  }
}

function getLineHeights(notePosition: number) {
  const linePositions = [];
  const direction = notePosition > 0 ? -1 : 1;
  const isBetweenLines = notePosition % 2 === 0;
  let lastPosition = notePosition;
  if (isBetweenLines) {
    lastPosition += direction;
  }
  while (lastPosition > 6 || lastPosition < -4) {
    linePositions.push(lastPosition);
    lastPosition += 2 * direction;
  }
  return linePositions.map((pos) => -NOTE_HEIGHT * (pos + A_OFFSET));
}

export function Note({ note, index, isCurrent }: NoteProps) {
  if (note.type === "Rest") {
    return (
      <g fill={isCurrent ? CURRENT_COLOR : undefined}>
        <Rest duration={note.duration} index={index} />
      </g>
    );
  }

  const notePosition = note.letter.charCodeAt(0) - ASCII_A + 7 * note.octave;
  const height = -NOTE_HEIGHT * (notePosition + A_OFFSET);
  const x = index * HORIZONTAL_SPACE;
  const NoteComponent = getNoteComponent(note);
  const dotted = isDotted(note.duration);
  const lineHeights = getLineHeights(notePosition);

  return (
    <g fill={isCurrent ? CURRENT_COLOR : undefined}>
      <NoteComponent x={x} y={height} />
      {note.sharp ? <Sharp x={x} y={height} /> : null}
      {dotted ? <Dot x={x} y={height} /> : null}
      {lineHeights.map((height) => (
        <Line x={x} y={height} />
      ))}
    </g>
  );
}
