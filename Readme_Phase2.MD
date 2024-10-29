# Phase 2 Sprint 8

## User/Admin stories:

## Admin User Stories:
- **As an admin,** I want to create and manage the new tables (users, profiles, votes, comments) to support authentication and user-related features, ensuring a smooth user experience.

- **As an admin,** I want to invalidate ideas, so that inappropriate content is not displayed on the app.

- **As an admin,** I want to invalidate user accounts, so that users who violate app guidelines are prevented from logging in.

## Authenticated User Stories:
- **As an authenticated user,** I want to log in easily and quickly using Google OAuth, so I can access the app securely without creating a separate account.

- **As an authenticated user,** I want to upvote or downvote posts, so that I can express my preferences regarding content.

- **As an authenticated user,** I want to leave text-based comments on posts, so that I can participate in discussions.

- **As an authenticated user,** I want to access and edit my profile, so that my information stays current.

- **As an authenticated user,** I want to be able to see the profiles of the users who create posts or comments.



## Updated user story-based tests (indicate manual vs. automated)

## Admin User Story-Based Tests:
- **Test 1: Manage Tables (Manual)**  
  Verify that the admin can create and manage new tables, ensuring they align with the new features for authentication and user profiles.

- **Test 2: Invalidate Ideas (Manual)**  
  Check that when an idea is invalidated, it no longer appears in user queries and ensure this action can be reversed.

- **Test 3: Invalidate User Accounts (Automated)**  
  Write a test that verifies the function correctly prevents a user from logging in after their account has been invalidated.

## Authenticated User Story-Based Tests:
- **Test 1: Google OAuth Login (Manual)**  
  Manually test that an authenticated user can log in using their Google account, and access is restricted to users with the "lehigh.edu" domain.

- **Test 2: Vote Functionality (Automated)**  
  Write tests that verify upvote and downvote actions, including changing votes and resetting to a neutral state.

- **Test 3: Comment on Posts (Automated)**  
  Verify that an authenticated user can add and edit comments on posts, and that comments are stored correctly in the database.

- **Test 4: Profile Editing (Manual)**  
  Manually test that users can edit their profile information, ensuring updates are reflected correctly.

- **Test 5: Profile Viewing (Manual)**  
  Verify that users can view the profiles of any user. Make sure that all information is shown and is correct, with the exception of gender identity and sexual orientation, which should not be shown (unless the profile being viewed is the user’s own).

## System Drawing:
![System Drawing](/docs/imgs/system2.png)
## Updated mock web/mobile UI drawings
![System Drawing](/docs/imgs/mock_ui_loginpage_phase2sprint8.png)
![System Drawing](/docs/imgs/mock_ui_imageboard_phase2sprint8.png)
![System Drawing](/docs/imgs/mock_ui_editprofile_phase2sprint8.png)
![System Drawing](/docs/imgs/mock_ui_commentform_phase2sprint8.png)

## Updated state machine drawing for user persona�s interaction with the application
![System Architecture Diagram](/docs/imgs/State_machine_diagram_1.png)
## NEW state machine drawing from perspective of an idea object (update, add comment, edit comment, like, repeated like, dislike, repeated dislike, delete comment)
![System Architecture Diagram](/docs/imgs/State_machine_diagram_2.png)

## Entity relationship Diagram
![Entity Diagram](/docs/imgs/Er2.png)

## Updated listing of routes, their purpose, and format of passed objects


## GET /ideas
### Purpose: 
Retrieve a list of ideas and comments and their attributes
### Request format: 

    {
        "mUserId": int
    }

#### Fields: 
* *mUserId* - The user ID of the user sending the GET request

### Response format: 

    {
        "mStatus": String,
        "mError": String,
        "mData": JSON object
        {
            "mIdeas": list of JSON
                [
                    {
                        "mId": int,
                        "mMessage": String,
                        "mAuthorId": int,
                        "mAuthorName": String,
                        "mUpvotes": int,
                        "mDownvotes": int,
                        "mVote": int
                    },
                    ...
                ],
            "mComments": list of JSON
                [
                    {
                        "mId": int,
                        "mIdeaId": int,
                        "mMessage": String,
                        "mAuthorId": int,
                        "mAuthorName": String
                    },
                    ...
                ]
        }
    }
#### Fields: 
* *mStatus* - Status of the request; either "ok" or "error"
* *mError* - Error message (null if mStatus is "ok", meaning there is no error)
* *mData* - The data being provided in the response
    * *mIdeass* - A list of all ideas and their attributes
        * *mId* - The ID of the idea
        * *mMessage* - The message content of the idea
        * *mAuthorId* - The user ID of the author of the idea
        * *mAuthorName* - The username of the author of the idea
        * *mUpvotes* - The number of upvotes on the idea
        * *mDownvotes* - The number of downvotes on the idea
        * *mVote* - Vote of the current user on the idea. 1 (or positive) if upvoted, -1 (or negative) if downvoted, 0 if neither.
    * *mComments* - A list of all comments and their attributes
        * *mId* - The ID of the comment
        * *mIdeaId* - The ID of the idea that is being commented on
        * *mMessage* - The message content of the comment
        * *mAuthorId* - The user ID of the author of the comment
        * *mAuthorName* - The username of the author of the comment


## GET /ideas/{id}
### Purpose: 
Retrieve one idea and its attributes
### Request format: 

    {
        "mUserId": int
    }

#### Fields: 
* *mUserId* - The user ID of the user sending the GET request

### Response format: 

    {
        "mStatus": String,
        "mError": String,
        "mData": JSON object
        {
            "mIdea": JSON object
                {
                    "mId": int,
                    "mMessage": String,
                    "mAuthorId": int,
                    "mAuthorName": String,
                    "mUpvotes": int,
                    "mDownvotes": int,
                    "mVote": int
                },
            "mComments": list of JSON
                [
                    {
                        "mId": int,
                        "mIdeaId": int,
                        "mMessage": String,
                        "mAuthorId": int,
                        "mAuthorName": String
                    },
                    ...
                ]
        }
    }
#### Fields: 
* *mStatus* - Status of the request; either "ok" or "error"
* *mError* - Error message (null if mStatus is "ok", meaning there is no error)
* *mData* - The data being provided in the response
    * *mIdea* - The idea being retrieved
        * *mId* - The ID of the idea
        * *mMessage* - The message content of the idea
        * *mAuthorId* - The user ID of the author of the idea
        * *mAuthorName* - The username of the author of the idea
        * *mUpvotes* - The number of upvotes on the idea
        * *mDownvotes* - The number of downvotes on the idea
        * *mVote* - Vote of the current user on the idea. 1 (or positive) if upvoted, -1 (or negative) if downvoted, 0 if neither.
    * *mComments* - A list of all comments on the idea and their attributes
        * *mId* - The ID of the comment
        * *mMessage* - The message content of the comment
        * *mAuthorId* - The user ID of the author of the comment
        * *mAuthorName* - The username of the author of the comment


## POST /ideas
### Purpose: 
Create a new idea posting
### Request format: 

    {
        "mUserId": int
        "mMessage": String
    }

#### Fields: 
* *mUserId* - The user ID of the user sending the POST request
* *mMessage* - The message content to include for the idea

### Response format: 

    {
        "mStatus": String,
        "mError": String,
        "mData": JSON object
        {
            "mIdea": JSON object
                {
                    "mId": int,
                    "mMessage": String,
                    "mAuthorId": int,
                    "mAuthorName": String,
                    "mUpvotes": int,
                    "mDownvotes": int,
                    "mVote": int
                },
            "mComments": list of JSON
                [
                    {
                        "mId": int,
                        "mMessage": String,
                        "mAuthorId": int,
                        "mAuthorName": String
                    },
                    ...
                ]
        }
    }
#### Fields: 
* *mStatus* - Status of the request; either "ok" or "error"
* *mError* - Error message (null if mStatus is "ok", meaning there is no error)
* *mData* - The data being provided in the response
    * *mIdea* - The new idea being created
        * *mId* - The ID of the idea
        * *mMessage* - The message content of the idea
        * *mAuthorId* - The user ID of the author of the idea
        * *mAuthorName* - The username of the author of the idea
        * *mUpvotes* - The number of upvotes on the idea ***(should be 0 for this response)***
        * *mDownvotes* - The number of downvotes on the idea ***(should be 0 for this response)***
        * *mVote* - Vote of the current user on the idea. 1 (or positive) if upvoted, -1 (or negative) if downvoted, 0 if neither ***(should be 0 for this response)***.
    * *mComments* - A list of all comments on the idea and their attributes ***(should be empty for this response)***
        * *mId* - The ID of the comment
        * *mMessage* - The message content of the comment
        * *mAuthorId* - The user ID of the author of the comment
        * *mAuthorName* - The username of the author of the comment

## POST /ideas/{id}/comments
### Purpose: 
Create a new comment on an idea
### Request format: 

    {
        "mUserId": int
        "mIdeaId": int
        "mMessage": String
    }

#### Fields: 
* *mUserId* - The user ID of the user sending the POST request
* *mIdeaId* - The ID of the idea that is being commented on
* *mMessage* - The message content to include for the comment

### Response format: 

    {
        "mStatus": String,
        "mError": String,
        "mData": JSON object
        {
            "mComment": JSON object
                {
                    "mId": int,
                    "mIdeaId": int,
                    "mMessage": String,
                    "mAuthorId": int,
                    "mAuthorName": String,
                },
        }
    }
#### Fields: 
* *mStatus* - Status of the request; either "ok" or "error"
* *mError* - Error message (null if mStatus is "ok", meaning there is no error)
* *mData* - The data being provided in the response
    * *mComment* - The new comment being created
        * *mId* - The ID of the comment
        * *mIdeaId* - The ID of the idea that is being commented on
        * *mMessage* - The message content of the comment
        * *mAuthorId* - The user ID of the author of the comment
        * *mAuthorName* - The username of the author of the comment

## PUT /ideas/{id}/upvote
### Purpose:
Upvote an idea
### Request format:
    {
        "mUserId": int
    }
#### Fields:
* *mUserId* - The user ID of the user sending the PUT request
### Response format:
    {
        "mStatus": String,
        "mError": String,
        "mData": JSON object
    
        {
            "mIdea": JSON object
                {
                    "mId": int,
                    "mUpvotes": int,
                    "mDownvotes": int,
                    "mVote": int
                },
        }
    }
#### Fields: 
* *mStatus* - Status of the request; either "ok" or "error"
* *mError* - Error message (null if mStatus is "ok", meaning there is no error)
* *mData* - The data being provided in the response
    * *mIdea* - The idea being retrieved
        * *mId* - The ID of the idea
        * *mUpvotes* - The new number of upvotes on the idea
        * *mDownvotes* - The new number of downvotes on the idea
        * *mVote* - Vote of the current user on the idea. 1 (or positive) if upvoted, -1 (or negative) if downvoted, 0 if neither ***(should be 1 for this response)***.

## PUT /ideas/{id}/downvote
### Purpose: 
Downvote an idea
### Request format:
    {
        "mUserId": int
    }
#### Fields:
* *mUserId* - The user ID of the user sending the PUT request
### Response format:
    {
        "mStatus": String,
        "mError": String,
        "mData": JSON object
    
        {
            "mIdea": JSON object
                {
                    "mId": int,
                    "mUpvotes": int,
                    "mDownvotes": int,
                    "mVote": int
                },
        }
    }
#### Fields: 
* *mStatus* - Status of the request; either "ok" or "error"
* *mError* - Error message (null if mStatus is "ok", meaning there is no error)
* *mData* - The data being provided in the response
    * *mIdea* - The idea being retrieved
        * *mId* - The ID of the idea
        * *mUpvotes* - The new number of upvotes on the idea
        * *mDownvotes* - The new number of downvotes on the idea
        * *mVote* - Vote of the current user on the idea. 1 (or positive) if upvoted, -1 (or negative) if downvoted, 0 if neither ***(should be -1 for this response)***.

## PUT /comments/{id}
### Purpose: 
Edit a comment
### Request format:
    {
        "mUserId": int,
        "mNewMessage": String
    }
#### Fields: 
* *mUserId* - The user ID of the user sending the PUT request
* *mNewMessage* - The new message content for the comment
### Response format: 

    {
        "mStatus": String,
        "mError": String,
        "mData": JSON object
        {
            "mComment": JSON object
                {
                    "mId": int,
                    "mIdeaId": int,
                    "mMessage": String,
                    "mAuthorId": int,
                    "mAuthorName": String,
                },
        }
    }
#### Fields: 
* *mStatus* - Status of the request; either "ok" or "error"
* *mError* - Error message (null if mStatus is "ok", meaning there is no error)
* *mData* - The data being provided in the response
    * *mComment* - The new comment being created
        * *mId* - The ID of the comment
        * *mIdeaId* - The ID of the idea that is being commented on
        * *mMessage* - The message content of the comment
        * *mAuthorId* - The user ID of the author of the comment
        * *mAuthorName* - The username of the author of the comment

## GET /user/{id}
### Purpose:
Get information about a user
### Request format:
    {
        "mUserId": int
    }
#### Fields:
* *mUserId* - The user ID of the user sending the GET request
### Response format: 
    {
        "mStatus": String,
        "mError": String,
        "mData": JSON object
        {
            "mOwner": boolean,
            "mUser": JSON object
                {
                    "mUserId": int,
                    "mUserName": String,
                    "mUserEmail": String,
                    "mUserGI": String,
                    "mUserSO": String,
                    "mUserNote": String
                }
        }
    }
#### Fields: 
* *mStatus* - Status of the request; either "ok" or "error"
* *mError* - Error message (null if mStatus is "ok", meaning there is no error)
* *mData* - The data being provided in the response
    * *mOwner* - Whether or not the user information being provided is about the current user. True if the current user "owns" this profile, False if not.
    * *mUser* - The user data being provided for the response
        * *mUserId* - The ID of the given user
        * *mUserName* - The username of the given user
        * *mUserEmail* - The email of the given user
        * **(OPTIONAL)** *mUserGI* - The gender identity of the given user. Only provided if the given user is the current user and "mOwner" is True.
        * **(OPTIONAL)** *mUserSO* - The sexual orientation of the given user. Only provided if the given user is the current user and "mOwner" is True.
        * *mUserNote* - The personal note of the given user.
## PUT /user/{id}
### Purpose:
Update/change information about a user
### Request format:
    {
        "mUserId": int,
        "mUserName": String,
        "mUserEmail": String,
        "mUserGI": String,
        "mUserSO": String,
        "mUserNote": String
    }
#### Fields:
* *mUserId* - The user ID of the user sending the GET request
* *mUserName* - The new username to change to
* *mUserEmail* - The new email to change to
* *mUserGI* - The new user gender identity to change to
* *mUserSO* - The new user sexual orientation to change to
* *mUserNote* - The new personal user note to change to
### Response format: 
    {
        "mStatus": String,
        "mError": String,
        "mData": JSON object
        {
            "mOwner": boolean,
            "mUser": JSON object
                {
                    "mUserId": int,
                    "mUserName": String,
                    "mUserEmail": String,
                    "mUserGI": String,
                    "mUserSO": String,
                    "mUserNote": String
                }
        }
    }
#### Fields: 
* *mStatus* - Status of the request; either "ok" or "error"
* *mError* - Error message (null if mStatus is "ok", meaning there is no error)
* *mData* - The data being provided in the response
    * *mOwner* - Whether or not the user information being provided is about the current user. True if the current user "owns" this profile, False if not.
    * *mUser* - The user data being provided for the response
        * *mUserId* - The ID of the given user
        * *mUserName* - The username of the given user
        * *mUserEmail* - The email of the given user
        * **(OPTIONAL)** *mUserGI* - The gender identity of the given user. Only provided if the given user is the current user and "mOwner" is True.
        * **(OPTIONAL)** *mUserSO* - The sexual orientation of the given user. Only provided if the given user is the current user and "mOwner" is True.
        * *mUserNote* - The personal note of the given user.

## POST /auth/weblogin
### Purpose:
Send ID token to server for authentication

## GET /auth/weblogout

## GET /auth/google/callback
## Updated Entity relationship diagram of the database tables and fields
https://lucid.app/lucidchart/bb642f5b-6855-418e-ad6d-f705a918baa4/edit?page=0_0&invitationId=inv_231d6c96-d58d-4a79-aef6-0e6c977e194e#
## Updated unit test DESCRIPTIONS for the backend, admin, web, and mobile (implementation not required for this sprint)

### Backend tests
* New post is given upvote/downvote count of 0
* No comments for a new post
* Correct behavior around votes:
    * Upvoting a post that is already upvoted gets rid of any vote
    * Downvoting a post that is already downvoted gets rid of any vote
    * Upvoting a post that is downvoted gets rid of upvote
    * Downvoting a post that is upvoted gets rid of downvote

### Admin tests:
- **Test 1: `createTables()`**
  **Description:** Tests that the function correctly sets up the new tables required for users, votes, comments, and profiles, ensuring compatibility with new features.
- **Test 2: `invalidateUser()`**
  **Description:** Verifies that a user’s account can be set to an "inactive" state, preventing login attempts while maintaining their data in the database.
- **Test 3: `invalidateIdea()`**
  **Description:** Ensures that invalidated ideas do not appear in any user queries but remain in the database for administrative purposes.

### Web frontend tests:
* Test: Clicking “like” when not liked: Changes the color of the button (for the correct idea/message)
* When like is clicked twice it becomes unliked(vice versa with dislike)
* When like/dislike it clicked other one is removed
* Profile can be clicked on for posts and comments
* Profiles other than own hides SO and GI

### Mobile frontend tests:
Mobile Test #1:
* Test that the user can log in
* We can test this by making sure our application uses OAuth so that people with lehigh.edu emails can access their profiles and the app
Mobile Test #2:
* Test that users can update their profiles
* We can do this by trying to edit certain profile fields (like username for example)
Mobile Test #3:
* Test that upvotes and downvotes work
* We can do this by making sure the upvotes/downvotes will increment/decrement when handled

## Thorough list of all backlog items for Phase 2 (including tech debt from Phase 1 and/or Phase 1')
Mobile:

Connect Mobile PUT and POST routes to database

Web:

From phase 1:

Frontend UI needs to be updated

Get rid of that "Placeholder" box, as we are no longer putting anything there most likely

Fix the colors

Change the like and dislike buttons to icons instead of basic HTML buttons

Whatever else you think makes sense

More unit tests are needed

There could be more commenting in the files

More console logs

Test backend is pretty barebones

Currently only has a hardcoded memory store with many hardcoded responses, only made to simply make sure that requests and responses work on the frontend, presuming the JSON formats are correct

Add more main methods

A main method that uses the proper in-memory datastore classes we created in the tutorials

A main method that actually connects to the database(?)

Add in another environment variable that you can set called MODE or something that allows you to quickly and easily choose which main method to run when running the backend

.gitignore might need to be updated idk

For phase 2:
Google OAuth
Downvotes
Comments
Profiles
Update how upvotes/downvotes work ie 1 upvote/downvote per user per idea

Backend:
    For phase 2:
Add new tables to database: Comments and Users
Google OAuth
Clean up backend code
Clean or get rid of main methods


Correct/update comments
Improve terminal output for new routes and requirements


Admin:
   From Phase 1:
.gitignore could probably use work
PM:
	-Organize jira board (possibly add new colors to color code for organization).

Team's Dokku URL(s): http://team-git-gud.dokku.cse.lehigh.edu

Team's software repo (bitbucket): https://bitbucket.org/sml3/cse216_fa24_team_21/src/main/

Team's Jira board: https://cse216-24sp-ala326.atlassian.net/jira/software/projects/GGT2/boards/35?atlOrigin=eyJpIjoiMjBiMDA4ZTY2ODMxNGQ3OWE1Yjc0ODMzNjllYThmOTQiLCJwIjoiaiJ9

