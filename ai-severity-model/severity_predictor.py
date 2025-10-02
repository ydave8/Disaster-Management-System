from flask import Flask, request, jsonify
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.naive_bayes import MultinomialNB
from sklearn.pipeline import make_pipeline
import pandas as pd

app = Flask(__name__)

# Sample training data (extend with more data later)
data = pd.DataFrame({
    "title": [
        "Flood alert in city", "Fire in building", "Power outage update",
        "Mild rain expected", "Explosion in factory", "Roadblock due to protest"
    ],
    "message": [
        "Water levels rising fast in river.", "Fire broke out in downtown factory.",
        "Power outage for 6 hours in area 5", "Light rain predicted in area 3",
        "Major explosion occurred causing injuries", "Protesters blocking highway"
    ],
    "severity": ["HIGH", "CRITICAL", "MEDIUM", "LOW", "CRITICAL", "HIGH"]
})

# Combine title and message
data['text'] = data['title'] + " " + data['message']

# ML model pipeline
model = make_pipeline(TfidfVectorizer(), MultinomialNB())
model.fit(data['text'], data['severity'])

@app.route('/predict-severity', methods=['POST'])
def predict():
    content = request.json
    title = content.get("title", "")
    message = content.get("message", "")
    combined = title + " " + message
    predicted = model.predict([combined])[0]
    return jsonify({"severity": predicted})

if __name__ == '__main__':
    app.run(port=5001)
