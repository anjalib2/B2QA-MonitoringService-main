from flask import Flask, jsonify
import sdv
import pandas as pd

app = Flask(__name__)

@app.route('/api/data')
def get_data():
activities_data = pd.read_csv(r"C:\Users\aishwarya\Downloads\activitiesDataDS.csv",
                              dtype={'accountId': 'str', 'activityId': 'str', 'contactId': 'str'})
activities_data.shape
print(activities_data.head(2))


activities_data = activities_data.sample(frac = 0.1)
activities_data['activityDate'] = pd.to_datetime(activities_data['activityDate'])
activities_data['activityDate'] = activities_data['activityDate'].dt.strftime('%m/%d/%Y')

cols_to_consider = ['rowId','contactId', 'activityId', 'activityType', 'activityTypeDescription', 'activityAttributes', 'activityDate', 'source', 'accountId', 'emailorwebsite']

activities_data = activities_data[cols_to_consider]

activities_data.shape

print(activities_data.head(2))

from sdv.metadata import SingleTableMetadata
metadata_obj = SingleTableMetadata.load_from_dict({
    "primary_key": "rowId",
    "METADATA_SPEC_VERSION": "SINGLE_TABLE_V1",
    "columns": {
        "contactId": { 
        "sdtype": "id",
        "regex_format": "U_[0-9]{10}"
    },  
        "rowId": { 
        "sdtype": "id",
        "regex_format": "U_[0-9]{10}"
    },
        "activityId": { 
        "sdtype": "id",
        "regex_format": "U_[0-9]{10}"
    },
        "activityType": {"sdtype": "categorical"},
        "activityTypeDescription": {"sdtype": "categorical"},
        "activityAttributes": {"sdtype": "categorical"},
        "activityDate": {"sdtype": "datetime", "datetime_format": '%m/%d/%Y'},
        "source": {"sdtype": "categorical"},
        "accountId": { 
        "sdtype": "id",
        "regex_format": "U_[0-9]{10}"
    },
        "emailorwebsite": {"sdtype": "email", "pii": True}
    }
})

metadata_obj

print(metadata_obj.primary_key)

metadata_obj.validate()

activities_data.shape

print(activities_data.head(2))

from sdv.single_table import GaussianCopulaSynthesizer
from sdv.lite import SingleTablePreset
model = SingleTablePreset(metadata_obj, name='FAST_ML')
model.fit(activities_data)

synthetic_data = model.sample(500)

print(synthetic_data.head(10))
    data = {synthetic_data}
    return jsonify(data)

if __name__ == '__main__':
    app.run(debug=True, host='localhost', port=5000)
