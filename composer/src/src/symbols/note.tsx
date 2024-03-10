import { Note as GNote, Rest as GRest, Duration } from "../grammar/grammar"
import { QuarterNote } from "./quarter_note"
import { EighthNote } from "./eighth_note"
import { WholeNote } from "./whole_note"
import { QuarterRest } from "./quarter_rest"
import { HalfNote } from "./half_note"
import { Dot } from "./dot"
import { Sharp } from "./sharp"

export type NoteProps = {
    note: GNote | GRest,
    index: number,
    isCurrent: boolean,
}

export type RestProps = {
    duration: Duration,
    index: number,
}

const NOTE_HEIGHT = 11.8
const ASCII_A = 97
const A_OFFSET = -2.2
const HORIZONTAL_SPACE = 60
const CURRENT_COLOR = "#8fff3d"

function Rest({duration, index}: RestProps) {
    const x = index * HORIZONTAL_SPACE
    switch (duration) {
        case Duration.Quarter:
            return <QuarterRest x={x} y={0} />
    }
}

function getNoteComponent(note: GNote) {
    switch (note.duration) {
        case Duration.Whole:
            return WholeNote
        case Duration.Quarter:
        case Duration.DottedQuarter:
            return QuarterNote
        case Duration.Eighth:
        case Duration.DottedEighth:
            return EighthNote
        case Duration.Half:
        case Duration.DottedHalf:
            return HalfNote
    }
}

function isDotted(duration: Duration) : boolean {
    switch (duration) {
        case Duration.DottedEighth:
        case Duration.DottedQuarter:
        case Duration.DottedHalf:
            return true
        default:
            return false
    }
}

export function Note({note, index, isCurrent}: NoteProps) {
    if (note.type === "Rest") {
        return (
            <g fill={isCurrent ? CURRENT_COLOR : undefined}>
                <Rest
                    duration={note.duration}
                    index={index}
                />
            </g>
        )
    }
    
    const height = -NOTE_HEIGHT * (
        note.letter.charCodeAt(0)
        - ASCII_A
        + A_OFFSET
        + 7 * note.octave
    )
    const x = index * HORIZONTAL_SPACE
    const NoteComponent = getNoteComponent(note)
    const dotted = isDotted(note.duration)
    
    return (
        <g fill={isCurrent ? CURRENT_COLOR : undefined}>
            <NoteComponent x={x} y={height} />
            {note.sharp ? <Sharp x={x} y={height} /> : null}
            {dotted ? <Dot x={x} y={height} /> : null}
        </g>
    )
}