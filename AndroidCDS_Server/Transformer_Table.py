


Transformer_Camera_IP = {"403":"10.41.0.199",
                      "4501":"10.41.0.168"
                      #"101":"10.41.0.170"
                      }
Transformer_Camera_ID = {"201":"203",
                         "203":"211",
                         "211":"212",
                         "212":"213",
                         "213":"214",
                         "214":"215",
                         "216":"245",
                         "245":"226",
                         "226":"225",
                         "225":"224",
                         "224":"223",
                         "223":"222",
                         "222":"221",
                         "221":"204",
                         "204":"202",
                         "202":"204",
                         "4521":"4525",
                         "4515":"4519",
                         "4509":"4513",
                         "4503":"4507",
                         "403":"4501",
                         "445":"403",
                         "4422":"445",
                         "4416":"4420",
                         "4410":"4414",
                         "4404":"4408",
                         "4401":"4402",
                         "402":"4401",
                         "434":"401",
                         "401":"4301",
                         "4303":"4307",
                         "4310":"4314",
                         "4316":"4320",
                         "4322":"463",
                         "463":"4601",
                         "4603":"404",
                         "404":"4607",
                         "4609":"4613",
                         "4615":"4619",
                         "4619":"4620",
                         "4526":"4527",
                         "4528":"4526",
                         "4531":"4527",
                         "4631":"4530",
                         "4628":"4630",
                         "4626":"4527",
                         "4326":"4626",
                         "4328":"4326",
                         "4331":"4327",
                         "4431":"4330",
                         "4428":"4430",
                         "4426":"4427"
                         }


def getTransformIP(transform):
    return Transformer_Camera_IP.get(transform)


def getTransformID(transform):
    return Transformer_Camera_ID.get(transform)