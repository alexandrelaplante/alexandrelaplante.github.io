import { Soundfont } from "smplr";
import { AudioContext as StandardizedAudioContext } from "standardized-audio-context";
import { Duration, Note, Rest } from "../grammar/grammar";


function convertDuration(d: Duration): number {
    return eval(d)
}

export class Violin {
    violin: Soundfont;
    constructor() {
        this.violin = this.load()
    }

    private load() : Soundfont {
        const context = new StandardizedAudioContext() as unknown as AudioContext;
        this.violin = new Soundfont(context, { instrument: "violin" });
        return this.violin
    }

    public resume() : void {
        this.violin.context.resume()
    }

    public play(note: Note | Rest) : Promise<void> {
        const duration = convertDuration(note.duration)
        if (note.type === 'Rest') {
            return new Promise<void>(resolve => {
                setTimeout(resolve, 1000*duration)
            });
        }
        const octave = (
            'ab'.includes(note.letter)
            ? 3 + note.octave
            : 4 + note.octave
        )
        const promise = new Promise<void>((resolve => {
            this.violin.start({
                note: `${note.letter}${note.sharp ? '#' : ''}${octave}`,
                onStart: () => {},
                onEnded: () => {resolve()},
                velocity: 80,
                duration: duration,
            })
        }))
        return promise
    }
}
