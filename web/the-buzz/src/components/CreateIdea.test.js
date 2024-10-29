import { fireEvent, render, screen } from '@testing-library/react';
import App from '../App.js';

test("CreateIdea correctly switches between states", () => {
    render(<App />);
    const createIdeaButton = screen.getByText(/Create Idea/i);
    fireEvent.click(createIdeaButton);
    expect(screen.getByPlaceholderText("What's your thoughts?")).toBeInTheDocument();
    const cancelIdeaButton = screen.getByText(/Cancel Idea/i);
    fireEvent.click(cancelIdeaButton);
    const createIdeaButton2 = screen.getByText(/Create Idea/i);
    expect(createIdeaButton2).toBeInTheDocument();
});

test("CreateIdea shows all correct inputs", () => {
    render(<App />);
    const createIdeaButton = screen.getByText(/Create Idea/i);
    fireEvent.click(createIdeaButton);
    const cancelIdeaButton = screen.getByText(/Cancel Idea/i);
    expect(cancelIdeaButton).toBeInTheDocument();
    const submitIdeaButton = screen.getByText(/Cancel Idea/i);
    expect(submitIdeaButton).toBeInTheDocument();
});