import { AST } from "./grammar/grammar";
import { GClef } from "./symbols/gclef";
import { Staff } from "./symbols/staff";
import { Note } from "./symbols/note";

type Props = {
  ast?: AST;
  current?: number;
  selected?: boolean[];
};

export function Visualizer({ ast, current, selected }: Props) {
  return (
    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1200 300" fill="#DDD">
      <Staff x={0} y={100} />
      <GClef x={-50} y={50} />
      {ast?.map((note, i) => (
        <Note
          key={i}
          note={note}
          index={i}
          isCurrent={i === current}
          isSelected={selected?.[i] || false}
        />
      ))}
    </svg>
  );
}
