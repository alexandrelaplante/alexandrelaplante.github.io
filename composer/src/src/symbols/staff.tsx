import { SymbolProps } from "./types";

export function Staff({ x, y }: SymbolProps) {
  return (
    <g transform={`translate(${x}, ${y}), scale(6,1)`}>
      <path d="m 0,98 0,-2 100,0 100,0 0,2 0,2 -100,0 -100,0 0,-2 z m 0,-24 0,-2 100,0 100,0 0,2 0,2 -100,0 -100,0 0,-2 z m 0,-24 0,-2 100,0 100,0 0,2 0,2 -100,0 -100,0 0,-2 z m 0,-24 0,-2 100,0 100,0 0,2 0,2 L 100,28 0,28 0,26 z M 0,2 0,0 100,0 200,0 200,2 200,4 100,4 0,4 0,2 z" />
    </g>
  );
}
