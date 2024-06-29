import { useContext, useState } from "react";
import "./App.css";
import { Grammar, AST, Note, Rest } from "./grammar/grammar.ts";
import { Visualizer } from "./visualizer.tsx";
import { ViolinContext } from "./audio/provider.tsx";

const ALL_NOTES: [string, boolean][] = [
  ["a", false],
  ["a", true],
  ["b", false],
  ["c", false],
  ["c", true],
  ["d", false],
  ["d", true],
  ["e", false],
  ["f", false],
  ["f", true],
  ["g", false],
  ["g", true],
];

function App() {
  const [text, setText] = useState("");
  const [current, setCurrent] = useState<number | undefined>();
  const [selected, setSelected] = useState<boolean[]>();
  const violin = useContext(ViolinContext);

  const g = new Grammar();
  const result = g.match(text);

  function isSelected(
    start: number | null,
    end: number | null,
    note: Note | Rest,
  ) {
    if (start === null || end === null) {
      return false;
    }
    const s0: number = start;
    const s1: number = end;

    return (
      // note          -------
      // selection -------
      (s0 <= note.range[0] && s1 >= note.range[0]) ||
      // note          -------
      // selection         -------
      (s0 <= note.range[1] && s1 >= note.range[1]) ||
      // note          -------
      // selection         -
      (s0 >= note.range[0] && s1 <= note.range[1])
    );
  }

  function onChange(e: React.ChangeEvent<HTMLTextAreaElement>) {
    setText(e.target.value);
  }

  async function play() {
    violin.resume();
    if (result?.ast?.length) {
      let i = 0;
      for (const note of result.ast) {
        setCurrent(i);
        await violin.play(note);
        i++;
      }
      setCurrent(undefined);
    }
  }

  function transposeUp() {
    if (result?.ast === undefined) {
      console.log("can't transpose");
      return;
    }

    const notes: AST = [];
    for (const [i, note] of result.ast.entries()) {
      if (note.type === "Rest") {
        notes.push(note);
      } else if (!selected?.[i]) {
        notes.push(note);
      } else {
        const newNote = Object.assign({}, note);
        const index = ALL_NOTES.findIndex(
          ([letter, sharp]) => note.letter === letter && note.sharp === sharp,
        );
        const newIndex = (index + 1) % ALL_NOTES.length;
        newNote.letter = ALL_NOTES[newIndex][0];
        newNote.sharp = ALL_NOTES[newIndex][1];
        if (newIndex < index) {
          newNote.octave += 1;
        }
        notes.push(newNote);
      }
    }
    setText(g.toString(notes));
  }

  function transposeDown() {
    if (result?.ast === undefined) {
      console.log("can't transpose");
      return;
    }

    const notes: AST = [];
    for (const [i, note] of result.ast.entries()) {
      if (note.type === "Rest") {
        notes.push(note);
      } else if (!selected?.[i]) {
        notes.push(note);
      } else {
        const newNote = Object.assign({}, note);
        const index = ALL_NOTES.findIndex(
          ([letter, sharp]) => note.letter === letter && note.sharp === sharp,
        );
        const newIndex = (ALL_NOTES.length + index - 1) % ALL_NOTES.length;
        newNote.letter = ALL_NOTES[newIndex][0];
        newNote.sharp = ALL_NOTES[newIndex][1];
        if (newIndex > index) {
          newNote.octave -= 1;
        }
        notes.push(newNote);
      }
    }
    setText(g.toString(notes));
  }

  return (
    <>
      <Visualizer ast={result.ast} current={current} selected={selected} />
      <div className="card">
        <textarea
          value={text}
          onChange={onChange}
          onBlur={(e) => {
            if (e.relatedTarget?.localName !== "button") {
              setSelected(undefined);
            }
          }}
          onSelect={(e) => {
            const target = e.target as HTMLInputElement;
            const start = target.selectionStart;
            const end = target.selectionEnd;
            setSelected(result?.ast?.map((n) => isSelected(start, end, n)));
          }}
        />
        <button onClick={play}>Play</button>
        <button
          onClick={() => {
            setText(
              "r r a8' c#8' d4.' c#8' b8' a8' f#2 e8 f#8 g4. f#8 e8 d8 b4 d4. b8",
            );
          }}
        >
          Farewell
        </button>
        <button onClick={transposeUp}>Up</button>
        <button onClick={transposeDown}>Down</button>
        <p className="error">{result.error}</p>
      </div>
    </>
  );
}

export default App;
