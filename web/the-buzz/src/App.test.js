import { render, screen } from '@testing-library/react';
import App from './App';

test('Create Idea button shows up', () => {
  render(<App />);
  const linkElement = screen.getByText(/Create Idea/i);
  expect(linkElement).toBeInTheDocument();
});

// test('IdeaRows show up', () => {
//   render(<App />);
//   const linkElement = screen.getByText(/Create Idea/i);
//   expect(linkElement).toBeInTheDocument();
// });
