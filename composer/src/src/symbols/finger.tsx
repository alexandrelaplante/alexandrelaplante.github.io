import { SymbolProps } from "./types";

interface FingerProps extends SymbolProps {
  num: String | number
}

export function Finger({ x, y, num }: FingerProps) {
  return (
    <g transform={`translate(${x + 125}, ${y})`}>
      <text fontSize="30">{num}</text>
    </g>
  );
}
