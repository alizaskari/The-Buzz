import { useState } from 'react';
import './CreateIdea.css';


/**
 * A component containing two states that allows for creation of a new message/idea. 
 * The inputType state is used to determine whether to display the button to create a new idea or to display the text box to input the new message
 * @param {*} props the state for the existing list of ideas
 * @returns a CreateIdea component
 */
function CreateIdea(props) {
    const [inputType, changeInput] = useState("button");
    return (
        <div className="create-idea">
            {inputType === "button" ? <CreateIdeaButton /> : <WriteIdeaBox state={props.state} />}
        </div>
    );

    /**
     * A simple button to activate the creation process for a new message
     * @returns a large button to switch to the WriteIdeaBox component
     */
    function CreateIdeaButton() {
        return (
            <>
                <button
                    className="create-idea-button"
                    type="button"
                    onClick={() => {
                        changeInput("write")
                    }}
                >
                    Create Idea
                </button>
            </>
        );
    }

    /**
     * A component with a text box and two buttons allowing you to type up a new message and then either submit or cancel that message.
     * The submit message button will call postMessage, sending the request to the backend to POST the new message
     * The cancel button will cancel the creation of the new message
     * Both buttons will return the CreateIdea component to being the CreateIdeaButton component
     * @param {*} props The properties to use, containing the state for the existing list of ideas
     * @returns A WriteIdeaBox component
     */
    function WriteIdeaBox(props) {
        const [ideas, setIdeas] = props.state;
        const [new_idea, setNewIdea] = useState('');

        /**
         * Handles a new idea being set when the input text is changed
         * @param {*} event The event, containing the new input for the idea
         */
        const handleChange = (event) => {
            setNewIdea(event.target.value);
        };
        return (
            <>
                <textarea
                    className="write-idea-box"
                    name="new_idea"
                    rows="10"
                    cols="5"
                    onChange={handleChange}
                    placeholder="What's your thoughts?"
                    maxLength="512"
                />
                <button
                    className="submit-idea-button"
                    type="button"
                    onClick={() => {
                        let msg = postMessage(new_idea, ideas, setIdeas);
                        //if (msg.mStatus == "ok") {
                        // setIdeas([...ideas, msg]);
                        // console.log(`Posted message ${msg}`);
                        changeInput("button");
                        //}

                        // const new_key = ideas[ideas.length - 1].id + 1;
                        // setIdeas([...ideas, { mId: new_key, mLikes: 0, mMessage: new_idea, }]);
                        // console.log("updated");
                        // changeInput("button");
                    }}
                >
                    Submit Idea
                </button>
                <button
                    className="cancel-idea-button"
                    type="button"
                    onClick={() => {
                        changeInput("button");
                    }}
                >
                    Cancel Idea
                </button>

            </>
        );

        /**
         * Sends a POST request to /messages with just the message parameter to request creation of a new Idea/message
         * @param {String} message the string to describe the idea
         * @param {Array} ideas the existing array of ideas
         * @param {function} setIdeas the state update function to change the list of ideas
         * @returns The newly created message/idea object, including the ID and like count (starting at 0)
         */
        async function postMessage(message, ideas, setIdeas) {
            let new_message;
            const doAjax = async () => {
                await fetch(`/messages`, {
                    method: 'POST',
                    body: JSON.stringify({
                        mMessage: message
                    }),
                    headers: {
                        'Content-type': 'application/json; charset=UTF-8'
                    }
                }).then((response) => {
                    // If we get an "ok" message, return the json
                    if (response.ok) {
                        return Promise.resolve(response.json());
                    }
                    // Otherwise, handle server errors with a detailed popup message
                    else {
                        window.alert(`The server replied not ok: ${response.status}\n` + response.statusText);
                    }
                    return Promise.reject(response);
                }).then((data) => {
                    console.log("Sent POST request successfully");
                    console.log(data);
                    new_message = data.mData;
                    setIdeas([...ideas, new_message]);
                    console.log(`Posted message ${new_message}`);
                }).catch((error) => {
                    console.warn('Something went wrong.', error);
                    window.alert("Unspecified error");
                });
            }

            // make the AJAX post and output value or error message to console
            doAjax().then(console.log).catch(console.log);
            return new_message;
        }
    }
}

export default CreateIdea;