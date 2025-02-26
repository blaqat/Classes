# Future Changes

## Reputation Systen:

### Table Changes:
	- the neighbor table will need something to track some value of reptutation with default value at 0

### API Methods:
	- addReputation(user, targetNeighbor, integer Amount) where the amount can be positive or negative

### Api Changes:
	- N/A


## Categorization System:

### Table Changes:
	- there would need to be a table that works as a dictionary for the list of tags
	- a linking table for tags and a wish/post

### API Methods:
	- addTag/s()
	- removeTag/s() adding tags to the post
	- getTags()	retrival of all tags on a post
	- hasTag() to check if the post is of the category

### API Changes:
	- the wish/item creation method will be changed to allow the addition of tags 
	- getFeed will need to be changed to allow deciding feed for specific tags