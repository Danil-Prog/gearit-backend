import json


def read_file(filename):
    try:
        with open(filename, 'r') as file:
            return json.load(file)
    except Exception as e:
        print(f"An unexpected error occurred: {e}")
        exit(1)


def generate_csv(data: json):
    factories_file = 'factories.csv'
    models_file = 'models.csv'
    factories_models_file = 'factories_models.csv'

    factories = {}

    idf = 1
    idm = 1

    for key in data:
        for current in data[key]:
            models = []

            for model in current['models']:
                models.append((idm, model['name'], model['year_from'], model['year_to']))
                idm += 1

            factories[
                (
                    idf,
                    current['name'],
                    current['year_from'],
                    current['year_to'],
                    current['country']
                )
            ] = models

            idf += 1

    with open(factories_file, 'w') as ff:
        ff.write(f'id;name;year_from;year_to;country\n')
        for f in factories:
            ff.write(f'{f[0]};{f[1]};{f[2]};{f[3]};{f[4]}\n')

    with open(models_file, 'w') as mf:
        mf.write(f'id;name;year_from;year_to\n')
        for f in factories:
            for m in factories[f]:
                mf.write(f'{m[0]};{m[1]};{m[2]};{m[3]}\n')

    with open(factories_models_file, 'w') as fmf:
        fmf.write(f'automobile_factory_id;automobile_model_id\n')
        for f in factories:
            for m in factories[f]:
                fmf.write(f'{f[0]};{m[0]}\n')


if __name__ == '__main__':
    data = read_file('automobiles.json')
    generate_csv(data)

    print('finish parse and generate file!')
