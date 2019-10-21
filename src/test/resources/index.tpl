<!DOCTYPE html>
<html>
    <script type="server/groovy">
        import com.myparser.model.Car
        def id = request.getParameter("id")
        car = Car.lookup(id)
    </script>
    <head>
        <title>${car.brand}</title>
    </head>
    <body>
        <h1 title="${car.brand}">${car.brand}</h1>
        <h2 data-if="car.ecoFriendly" title="${car.fuelType}">Fuel Type:
            ${car.fuelType}</h2>
        <div data-loop-model="car.models">Model: ${model}</div>
    </body>
</html>

