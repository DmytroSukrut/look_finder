<h1 style="text-align: center;">✨ Look Finder ✨</h1>
<h3 style="text-align: center; color: lightpink;">Created by Dmytro Sukrut</h3>

<p style="text-align: center; font-size: 18px;">
  A modern website that finds clothes based on your parameters.<br>
  Powered by <b>Java</b> (backend) and <b>Vite + React</b> (frontend).
</p>

---

<h2 style="text-align: center; color: aqua;">🔧 Backend Overview</h2>

<p style="text-align: center;">
  All backend files are located in:
</p>
<p style="text-align: center;">
src/main/java/com/look_finder/
</p>

---

### **GetBershkaClothes.java**
<p style="font-size: 14px;">
This class exposes the main API endpoint for Bershka filtering.
</p>

**Endpoint example:**

/api/clothes/bershka/filter?category=jeans_w&sizeD=36&sizeS=M

**Returns:**  
Fully parsed Bershka JSON response adapted for your frontend.

---

### **BershkaService.java**
<p style="font-size: 14px;">
Core service responsible for fetching and parsing Bershka data.
</p>

**What it does:**

1. Fetches Bershka stock data and parses it using `UrlCreatorBershka`.
2. Fetches full product data for each product ID.
3. Parses results using `BershkaParcer`.
4. Combines all necessary information and returns ready-to-use JSON.

**Parameters:**

- `category` - category to filter (e.g., `"jeans_w"`)
- `sizeD` - digit size (e.g., `36`)
- `sizeS` - text size (e.g., `M`)

**Returns:**  
Parsed JSON containing all product data for the frontend.

**Throws:**  
`IOException` - if Bershka servers fail or the request cannot be completed.

---

### **UrlCreatorBershka.java**
<p style="font-size: 14px;">
Creates url with all products id
</p>

**What it does:**

1. Parses data from `stock` endpoint in bershka
2. <b>Deletes</b> duplicates of products
3. Creates url to fetch further with all necessary product ids

**Parameters:**

- `json` - raw stock JSON, which contains all bershkas stocks
- `category` - category to put in url (e.g., `"jeans_w"` appends `1010276029`)

**Returns:**
Url to fetch in service

---

### **BershkaParcer.java**
<p style="font-size: 14px;">
Component which paces original bershka JSON
</p>

**What it does:**

Extracts necessary information from bershka JSON:
name, id, all colors, price, size, photos.

**Parameters:**

- `json` - raw bershkas JSON, which contains all information about products
- `sizeD` - digit size (e.g., `36`)
- `sizeS` - text size (e.g., `M`)

**Returns:**
Subtracted JSON, which contains only necessary information

---