import Idea from './Idea.js';

/**
 * A component that contains a set of rows, with each rows being one idea component
 * @param {*} props An object that contains the list of ideas/messages to display
 * @returns A div containing all the provided ideas as Idea components
 */
function IdeaRows(props) {
    const ideas = props.ideas;

    return (
        <div>
            {ideas.map((idea) => (
                <Idea id={idea.mId} likes={idea.mLikes} message={idea.mMessage} />
            ))}
        </div>
    )
}

export default IdeaRows;