import { useContext, useState } from "react";
import "./App.css";
import { Grammar } from "./grammar/grammar.ts";
import { Visualizer } from "./visualizer.tsx";
import { ViolinContext } from "./audio/provider.tsx";

function App() {
  const [text, setText] = useState("");
  const [current, setCurrent] = useState<number | undefined>();
  const violin = useContext(ViolinContext);

  const g = new Grammar();
  const result = g.match(text);

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

  console.log(result?.ast);

  return (
    <>
      <Visualizer ast={result.ast} current={current} />
      <div className="card">
        <textarea value={text} onChange={onChange} />
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
        <p className="error">{result.error}</p>
      </div>
    </>
  );
}

export default App;
