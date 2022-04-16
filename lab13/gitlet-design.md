# Gitlet Design Document
author: Elijah Jacob

## 1. Classes and Data Structures

##Commands
Contains all the methods required to run the program
###init()
####Variables used:
* Current Branch info
* Commit ID
* Parent ID

System should start off with one commit with no details 
Creates a directory for the Gitlet folder 
Commit 0 is initialized without any link to a blob (or contents)
Master is the active branch 
### add()
Sends the file to staging, if it is already in the staging and it is re-added, the original contents overwritten 
####Variables used: 
* _cdirectory
* Commit ID
* Parent ID

### commit()
A new branch is added, staging is cleared 
Each commit can be identified with its SHA-1 hash ID
####Variables used:
* SHA1 Hash ID
* Blob reference keys
* commit reference keys 
* Message 
* Timestamp
####Methodology:
1. The blob is removed from the staging area
2. The Head is set to the current commit
3. The hashmap value is set to the blob key 

### log()
Displays information about all of the commits. 
Commits are registered with the SHA-1 Hash key
####Variables used: 
* Current commit 
* Timestamp
* Message

### checkout()
Replaces the code in the working directory from the HEAD of the commits.
The head wil now point to the front of the list
####Variables used:
* Branch ID
* Commit ID

## Diff
A comparison of two sequences of strings.  After executing setSequences to initialize the data, methods allow computing longest common sequences and differences in the form of edits needed to convert one sequence to the next.

## Blob
Uses a key map to store the contents of the file (blobs)
####Variables used:
* name/key 
* hashCode/value 
* contents

The blob is initialized and set in the add method
## Commit 
## DumpObj
## Main
Where the driver code is found and the input commands are registered. Validation is also included.
## UnitTest
Tests we would check for are for accuracy with:
1. Commits
2. Blobs
3. A test for every function 
## Utils 
This class contains all the functions required for file processing 
                                                  
##Branches                                            
Contains the code that initializes and sets branches with the LinkedList data structure 

##Staging Area                                                                                   
Contains the code that creates the staging area and separates what is staged for removal. Uses two HashMaps to see what files are staged for addition and what are staged for removal.      

## 2. Algorithms

This is where you tell us how your code works. For each class, include
a high-level description of the methods in that class. That is, do not
include a line-by-line breakdown of your code, but something you would
write in a javadoc comment above a method, ***including any edge cases
you are accounting for***. We have read the project spec too, so make
sure you do not repeat or rephrase what is stated there.  This should
be a description of how your code accomplishes what is stated in the
spec.

The length of this section depends on the complexity of the task and
the complexity of your design. However, simple explanations are
preferred. Here are some formatting tips:

* For complex tasks, like determining merge conflicts, we recommend
  that you split the task into parts. Describe your algorithm for each
  part in a separate section. Start with the simplest component and
  build up your design, one piece at a time. For example, your
  algorithms section for Merge Conflicts could have sections for:

   * Checking if a merge is necessary.
   * Determining which files (if any) have a conflict.
   * Representing the conflict in the file.
  
* Try to clearly mark titles or names of classes with white space or
  some other symbols.

###add()
1. The blob is initialized and set
2. The blob is placed in the current working directory
3. The file is added to a hashmap
4. The file is linked to Blob and placed in the staging area


###init()
1. setup persistence
2. create the gitlet VCS
3. initialize the gitlet folder directory
4. create a directory for commits
5. create a new commit
6. setup branches by using pointers to keep track of the current branchese

###commit()
1. The blob is removed from the staging area
2. The Head is set to the current commit
3. The hashmap value is set to the blob key


###log()
1. Display current commit 
2. Displays previous commits in the LinkedList going back to the HEAD.

###checkout()                    
1. Puts all files from the HEAD into the CWD, whilst overwriting files 
2. Set the Head to the front of the commit links

## 3. Persistence

###Commit()
After running the commit function, I would need to ensure the state of my program remains the same across multiple runs. It would be hard to maintain the commit tree without keeping game state after a run.

## 4. Design Diagram


![](/Users/elijahjacob/Downloads/IMG_7818.HEIC)
![](/Users/elijahjacob/Downloads/IMG_7819.HEIC)
